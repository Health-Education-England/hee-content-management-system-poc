const parser = require('fast-xml-parser');
const fs = require('fs');
const FormData = require('form-data');
const core = require('@actions/core');
const path = require('path');
const axios = require('./axios');
const authentication = require('./authentication');

const axiosInstance = axios.create();


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

    const distId = await uploadDistributionToBRC(core.getInput('distPath'), accessToken);

    core.setOutput('distributionId', distId);
    console.log(`Upload process finished with success. DistributionID = ${distId}`);
  } catch (error) {
    core.setFailed(error.message);
  }
}

uploadDistribution();
