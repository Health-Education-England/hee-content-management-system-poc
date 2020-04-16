const core = require('@actions/core');
const authentication = require('./authentication');

async function login() {
  try {
    console.log('Start login process');
    const {
      accessToken,
      refreshToken,
    } = await authentication.login(process.env.BRC_USERNAME, process.env.BRC_PASSWORD);

    core.setOutput('accessToken', accessToken);
    core.setOutput('refreshToken', refreshToken);
    console.log('Login process finished with success');
  } catch (error) {
    core.setFailed(error.message);
  }
}

login();
