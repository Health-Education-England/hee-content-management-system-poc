const axios = require('axios');

const BRC_STACK = process.env.stack;
const BASE_URL = `https://api-${BRC_STACK}.onehippo.io`;

let authRefreshToken;
let accessToken;

async function getAccessToken() {
    if (accessToken) {
        if (!await isAccessTokenValid(accessToken)) {
            accessToken = await refreshToken(authRefreshToken);
        }
    } else {
        await login();
    }

    return accessToken;
}

async function login() {
    const authPath = `${BASE_URL}/v3/authn/access_token`;

    const response = await axios.post(
        authPath,
        {
            "username": "",
            "password": ""
        }
    );
    console.log(response.data);

    let authResponse = response.data;
    accessToken = `${authResponse.token_type} ${authResponse.access_token}`;
    authRefreshToken = authResponse.refresh_token;
}

async function isAccessTokenValid(accessToken) {
    let validateAccessTokenURL = `${BASE_URL}/v3/authn/verify_token`;
    let config = createAuthorizationRequestHeader(accessToken);

    try {
        await axios.post(
            validateAccessTokenURL,
            config
        );
        return true;
    } catch (e) {
        return false;
    }
}

async function refreshToken(refreshToken) {
    let refreshTokenURL = `${BASE_URL}`;
    let refreshTokenBody = {
        "grant_type": "refresh_token",
        "refresh_token": refreshToken
    };

    let response = await axios.post(
        refreshTokenURL,
        refreshTokenBody
    );
    return `${response.data.grant_type} ${response.data.access_token}`
}

async function createAuthorizationRequestHeader(accessToken) {
    return {
        headers: {
            Authorization: accessToken
        }
    }
}

module.exports = {
    getAccessToken
};