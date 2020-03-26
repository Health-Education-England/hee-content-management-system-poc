const axios = require('axios');
const parser = require('fast-xml-parser');
const fs = require('fs');
const FormData = require('form-data');

const brcAuth = require('./brcAuth');

const BRC_STACK = process.env.STACK_NAME;
const BCR_API_BASE_URL = `https://api-${BRC_STACK}.onehippo.io`;

const axiosInstance = axios.create();

axiosInstance.interceptors.request.use(
    async (config) => {
        config.headers['Content-Type'] = 'application/json';
        config.headers['Authorisation'] = await brcAuth.getAccessToken();

        return config;
    },
    error => {
        Promise.reject(error)
    }
);

/**
 * Upload current project distribution to BRC.
 * @param workspace root path of the project
 * @returns {Promise<void>} distributionId
 */
async function uploadDistribution(workspace) {
    const distribution = getDistribution(workspace);
    const distId = await uploadDistributionToBRC(distribution);

    console.log(`distId: ${distId}`);
    return distId;
}

function getDistribution(workspace) {
    const pomXml = fs.readFileSync(`${workspace}/pom.xml`, 'utf8');
    const pomJson = parser.parse(pomXml);
    const projectName = pomJson.project.artifactId;
    const projectVersion = pomJson.project.version;

    return `${workspace}/target/${projectName}-${projectVersion}-distribution.tar.gz`;
}

async function uploadDistributionToBRC(distribution) {
    console.log(`Upload distribution ${distribution}`);

    const formData = new FormData();
    const stream = fs.createReadStream(distribution);
    formData.append("dist_file", stream);

    const formHeaders = formData.getHeaders();

    const config = {
        headers: formHeaders,
        maxBodyLength: Number.POSITIVE_INFINITY,
        maxContentLength: Number.POSITIVE_INFINITY
    };
    const response = await axiosInstance.post(
        `${BCR_API_BASE_URL}/v3/distributions/`,
        formData,
        config
    );
    console.log(`Upload result: ${JSON.stringify(response.data)}`);
    return response.data.id;
}

/**
 * Deploy a given distribution to a given environment
 * @param distId
 * @param environmentName
 * @returns {Promise<void>}
 */
async function deploy(distId, environmentName) {
    const environments = await getEnvironments();
    const environmentId = getEnvironmentId(environments, environmentName);
    console.log(`Environment ID: ${environmentId}`);

    if (!environmentId) {
        console.log(`Deploy suspended! No environment with name ${environmentName} has been found.`);
        return;
    }

    await deployToBRC(distId, environmentId);
    console.log("Deployed application with success");
}

async function deployToBRC(distId, environmentId) {
    console.log("Deploy distribution");

    const body = {
        "distributionId": distId,
        "strategy": "stopstart"
    };

    let response = await axiosInstance.put(
        `${BCR_API_BASE_URL}/v3/environments/${environmentId}/deploy`,
        body
    );

    console.log(`Result of deploy: ${response.status} ${JSON.stringify(response.data)}`);
}

async function getEnvironments() {
    console.log("Get the environments");

    const envResponse = await axiosInstance.get(`${BCR_API_BASE_URL}/v3/environments`);

    console.log(`Result of get environments request: ${envResponse.status} ${JSON.stringify(envResponse.data)}`);
    return envResponse.data.items;
}

function getEnvironmentId(environmentList, environmentName) {
    let envId;

    const filteredEnvs = environmentList.filter(
        ({name}) => name === environmentName
    );

    if (filteredEnvs.length > 0) {
        envId = filteredEnvs[0];
    }

    return envId;
}