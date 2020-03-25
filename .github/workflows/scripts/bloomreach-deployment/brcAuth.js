const axios = require('axios');

const BRC_STACK = process.env.stack;
const BASE_URL = `https://api-${BRC_STACK}.onehippo.io`;

let authRefreshToken;
let accessToken;

async function getAccessToken() {
    if (accessToken) {
        if (!await isAccessTokenValid(accessToken)) {
            await refreshToken(authRefreshToken);
        }
    } else {
        await login();
    }

    return accessToken;
}

async function login() {
    console.log("Retrieve BRC access token.");
    const authURL = `${BASE_URL}/v3/authn/access_token`;

    const response = await axios.post(
        authURL,
        {
            "username": process.env.BRC_USERNAME,
            "password": process.env.BRC_PASSWORD
        }
    );
    console.log(`Access token response ${JSON.stringify(response)}`);

    let authResponse = response.data;
    console.log(`Set access token and refresh token`);
    accessToken = `${authResponse.token_type} ${authResponse.access_token}`;
    authRefreshToken = authResponse.refresh_token;
}

async function isAccessTokenValid(accessToken) {
    console.log("Validate access token");
    let validateAccessTokenURL = `${BASE_URL}/v3/authn/verify_token`;
    let config = {
        headers: {
            Authorization: accessToken
        }
    };

    try {
        await axios.post(
            validateAccessTokenURL,
            config
        );
        console.log("Access token is valid");
        return true;
    } catch (e) {
        console.log("Access token is not valid.");
        return false;
    }
}

async function refreshToken(refreshToken) {
    console.log("Refresh token");
    let refreshTokenURL = `${BASE_URL}`;
    let refreshTokenBody = {
        "grant_type": "refresh_token",
        "refresh_token": refreshToken
    };

    let response = await axios.post(
        refreshTokenURL,
        refreshTokenBody
    );
    console.log("Received new access token.");
    accessToken = `${response.data.grant_type} ${response.data.access_token}`
}

module.exports = {
    getAccessToken
};