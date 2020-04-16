const core = require('@actions/core');
const axios = require('./axios');
const authentication = require('./authentication');

const axiosInstance = axios.create();

async function deployToBRC(distId, environmentId, accessToken) {
  console.log('Deploy distribution to BRC.');

  const body = {
    distributionId: distId,
    strategy: 'stopstart',
  };

  await axiosInstance.put(
    `/v3/environments/${environmentId}/deploy`,
    body,
    {
      headers: {
        Authorization: accessToken,
      },
    },
  );

  console.log('Deploy request completed.');
}

async function getEnvironmentId(accessToken, environmentName) {
  const response = await axiosInstance.get(
    '/v3/environments?name=' + environmentName,
    {
      headers: {
        Authorization: accessToken,
      }
    }
  );

  return response.data.id;
}

/**
 * Deploy a given distribution to a given environment
 * @returns {Promise<void>}
 */
async function deploy() {
  const accessToken = await authentication.getTokenOrRefresh(
    core.getInput('accessToken'),
    core.getInput('refreshToken'),
  );
  core.setOutput('accessToken', accessToken);

  const distId = core.getInput('distId');
  const environmentName = core.getInput('environmentName');

  const environmentId = await getEnvironmentId(accessToken, environmentName);
  console.log(`Environment ID: ${environmentId}`);

  /* if (!environmentId) {
    throw new Error(`Deploy suspended! No environment with name ${environmentName} has been found.`);
  } */

  return deployToBRC(distId, environmentId, accessToken);
}

async function deployAndCatchError() {
  try {
    console.log('Start deployment process');
    await deploy();
    console.log('Deployment finished with success!');
  } catch (error) {
    core.setFailed(error.message);
  }
}

deployAndCatchError();
