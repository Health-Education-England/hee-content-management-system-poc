const axios = require('axios');

function create() {
  const stackName = process.env.STACK_NAME;
  const baseURL = `https://api-${stackName}.onehippo.io`;

  const instance = axios.create({
    baseURL,
    headers: { 'Content-Type': 'application/json' },
  });

  instance.interceptors.response.use((response) => response, (error) => {
    let errorMessage = 'An error has occurred during BRC API communication. Please see more details below:';

    if (error.response) {
      errorMessage += `\nResponse: ${JSON.stringify(error.response.data, null, 2)} `;
      errorMessage += `\nStatus: ${JSON.stringify(error.response.status, null, 2)}`;
      errorMessage += `\nHeaders: ${JSON.stringify(error.response.headers, null, 2)}`;
    } else if (error.request) {
      errorMessage += 'No response received.';
    } else {
      errorMessage += `Unknown error: ${error.message}`;
    }

    errorMessage += `\nRequest Config: ${JSON.stringify(error.config, null, 2)}`;

    return Promise.reject(new Error(errorMessage));
  });

  return instance;
}

module.exports = {
  create,
};
