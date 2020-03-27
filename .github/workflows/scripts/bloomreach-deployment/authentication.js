const axios = require('./axios');

const axiosInstance = axios.create();

async function isAccessTokenValid(accessToken) {
  console.log('Validate access token');
  const validateAccessTokenURL = '/v3/authn/verify_token';
  const config = {
    headers: {
      Authorization: accessToken,
    },
  };

  try {
    await axiosInstance.get(
      validateAccessTokenURL,
      config,
    );

    console.log('Access token is valid');
    return true;
  } catch (e) {
    console.log('Access token is not valid.');
    return false;
  }
}

async function getRefreshedToken(refreshToken) {
  console.log('Refresh token');
  const refreshTokenURL = '/v3/authn/refresh_token';
  const refreshTokenBody = {
    grant_type: 'refresh_token',
    refresh_token: refreshToken,
  };

  const response = await axiosInstance.post(
    refreshTokenURL,
    refreshTokenBody,
  );
  console.log('Received new access token.');
  return `Bearer ${response.data.access_token}`;
}

async function login(username, password) {
  console.log('Retrieve BRC access token.');
  const authURL = '/v3/authn/access_token';

  const response = await axiosInstance.post(
    authURL,
    {
      username,
      password,
    },
  );
  console.log('Received access token');

  const authResponse = response.data;
  console.log('Set access token and refresh token');

  return {
    accessToken: `Bearer ${authResponse.access_token}`,
    refreshToken: authResponse.refresh_token,
  };
}

async function getTokenOrRefresh(accessToken = null, refreshToken = null) {
  let newAccessToken = accessToken;

  if (!await isAccessTokenValid(accessToken)) {
    newAccessToken = await getRefreshedToken(refreshToken);
  }

  return newAccessToken;
}

module.exports = {
  getTokenOrRefresh,
  login,
};
