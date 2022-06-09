const https = require('https');
const User = require('../model/user');
const jwt = require('jsonwebtoken');
const AuthFacebook = async (req, res, next) => {
  const access_token = req.body.access_token;
  const options = {
    hostname: 'graph.facebook.com',
    port: 443,
    path: '/me?access_token=' + access_token,
    method: 'GET'
  }
  const request = https.get(options, response => {
    response.on('data', async (user) => {
      user = JSON.parse(user.toString());
      const { name, id } = user;
      const data = await User.findOne({ facebookId: id });
      if (!data) {
        const newaccount = await User.create({ facebookId: id, name: name });
        const token = newaccount.CreateJWT();
        return res.status(200).json({ user: { name: newaccount.name }, token: token });
      }
      else {
        const token = jwt.sign({ userId: id, name: name }, process.env.JWT_SECRET, {
          expiresIn: process.env.JWT_LIFETIME
        })
        return res.status(200).json({ user: { name: name }, token: token });
      }
    });
  })
  request.on('error', (message) => {
    console.error(message);
  });
  request.end();
}

module.exports = { AuthFacebook }