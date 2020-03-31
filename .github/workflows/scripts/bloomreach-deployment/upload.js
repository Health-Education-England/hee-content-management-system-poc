const parser = require('fast-xml-parser');
const fs = require('fs');
const FormData = require('form-data');
const core = require('@actions/core');
const path = require('path');
const axios = require('./axios');
const authentication = require('./authentication');

const axiosInstance = axios.create();


function getDistribution() {
  const workspace = process.env.GITHUB_WORKSPACE;
  const commitSHAabbrev = process.env.GITHUB_SHA.substring(0, 7);
  const pomXmlPath = path.join(workspace, 'pom.xml');

  const pomXml = fs.readFileSync(pomXmlPath, 'utf8');
  const pomJson = parser.parse(pomXml);

  const projectName = pomJson.project.artifactId;
  const projectVersion = pomJson.project.version;

  return `${workspace}/target/${projectName}-${projectVersion}-${commitSHAabbrev}-distribution.tar.gz`;
}

async function uploadDistributionToBRC(distribution, accessToken) {
  console.log(`Upload distribution ${distribution} to BRC.`);

  const formData = new FormData();
  const stream = fs.createReadStream(distribution);
  formData.append('dist_file', stream);

  const formHeaders = formData.getHeaders();

  const config = {
    headers: formHeaders,
    maxBodyLength: Number.POSITIVE_INFINITY,
    maxContentLength: Number.POSITIVE_INFINITY,
  };
  config.headers.Authorization = accessToken;

  const response = await axiosInstance.post(
    '/v3/distributions/',
    formData,
    config,
  );
  console.log('Upload request completed.');
  return response.data.id;
}

/**
 * Upload current project distribution to BRC.
 * @returns {Promise<void>} distributionId
 */
async function uploadDistribution() {
  try {
    console.log('Start upload process');

    const accessToken = await authentication.getTokenOrRefresh(
      core.getInput('accessToken'),
      core.getInput('refreshToken'),
    );
    core.setOutput('accessToken', accessToken);

    const distribution = getDistribution();
    const distId = await uploadDistributionToBRC(distribution, accessToken);

    core.setOutput('distributionId', distId);
    console.log(`Upload process finished with success. DistributionID = ${distId}`);
  } catch (error) {
    core.setFailed(error.message);
  }
}

uploadDistribution();
