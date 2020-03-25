const axios = require('axios');
const parser = require('fast-xml-parser');
const brcAuth = require('./brcAuth');

const BRC_STACK = process.env.stack;
const BCR_API_BASE_URL = `https://api-${BRC_STACK}.onehippo.io`;

console.log("BloomReach Deployment");

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
    const pomJson = parser.convertToJson(`${workspace}/pom.xml`);
    const projectName = pomJson.project.artifactId;
    const projectVersion = pomJson.project.version;
    return `${workspace}/target/${projectName}-${projectVersion}-distribution.tar.gz`;
}

async function uploadDistributionToBRC(distribution) {
    console.log(`Upload distribution ${distribution}`);

    const formData = new FormData();
    formData.append("dist_file", distribution);

    const response = await axios.post(
        `${BCR_API_BASE_URL}/v3/distributions/`,
        formData,
        { headers: {'Content-Type': 'multipart/form-data' }}
    );

    console.log(`Upload result: ${JSON.stringify(response.data)}`);
    response.data.id;
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
        return ;
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

   let response = axios.put(
       `${BCR_API_BASE_URL}/v3/environments/${environmentId}/deploy`,
       body
   );

   console.log(`Result of deploy: ${JSON.stringify(response)}`);
}

async function getEnvironments() {
    console.log("Get the environments");

    const envPath = `${BCR_API_BASE_URL}/v3/environments`;
    const envResponse = await axios.get(envPath);

    console.log(`Result of get environments request: ${JSON.stringify(envResponse)}` );
    return envResponse.data.items;
}

function getEnvironmentId(environmentList, environmentName) {
    let envId;

    const filteredEnvs = environmentList.filter(({name}) => name === environmentName);

    if (filteredEnvs.length > 0) {
        envId = filteredEnvs[0];
    }

    return envId;
}

axios.interceptors.request.use(
    async (config) => {
        config.headers['Content-Type'] = 'application/json';
        config.headers['Authorisation'] =  await brcAuth.getAccessToken();

        return config;
    },
    error => {
        Promise.reject(error)
    }
);