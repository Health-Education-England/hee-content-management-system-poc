const axios = require('axios');

console.log("BloomReach Deployment");


function login() {
    let access_token;

    axios.post('https://api-hee.onehippo.io/v3/authn/access_token', {
        "username": "pat.shone@manifesto.co.uk",
        "password": "wZk4RcafbUpe6WuL"
    })
    .then(function (response) {
        console.log(response);
        console.log(response.)
    })
    .catch(function (error) {
        console.log(error);
    })

}