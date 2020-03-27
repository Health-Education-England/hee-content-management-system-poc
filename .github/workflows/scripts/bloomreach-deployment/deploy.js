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

async function getEnvironments(accessToken) {
  console.log('Get BRC environments.');

  const envResponse = await axiosInstance.get(
    '/v3/environments',
    {
      headers: {
        Authorization: accessToken,
      },
    },
  );

  console.log(`Received ${envResponse.data.length} environments.`);
  return envResponse.data.items;
}

function getEnvironmentId(environmentList, environmentName) {
  let envId;

  const filteredEnvs = environmentList.filter(
    ({ name }) => name === environmentName,
  );

  if (filteredEnvs.length > 0) {
    envId = filteredEnvs[0].id;
  }

  return envId;
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

  const environments = await getEnvironments(accessToken);
  const environmentId = getEnvironmentId(environments, environmentName);
  console.log(`Environment ID: ${environmentId}`);

  if (!environmentId) {
    throw new Error(`Deploy suspended! No environment with name ${environmentName} has been found.`);
  }

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
