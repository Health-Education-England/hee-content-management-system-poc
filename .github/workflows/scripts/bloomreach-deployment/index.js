const axios = require('axios');

console.log("BloomReach Deployment");


function login() {
    let access_token;

    axios.post('https://api-hee.onehippo.io/v3/authn/access_token', {
        "username": "",
        "password": ""
    })
    .then(function (response) {
        console.log(response);
        console.log(response.)
    })
    .catch(function (error) {
        console.log(error);
    })

}
