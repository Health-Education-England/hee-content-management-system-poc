const axios = require('axios');
const brcAuth = require('./brcAuth');

const BRC_STACK = process.env.stack;
const BASE_URL = `https://api-${BRC_STACK}.onehippo.io`;

console.log("BloomReach Deployment");

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

async function getEnvironments(accessToken) {
    const envPath = `${BASE_URL}/v3/environments`;

    const envResponse = await axios.get(envPath);
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