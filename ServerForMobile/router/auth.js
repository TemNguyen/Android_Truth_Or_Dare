const express = require('express');
const router = express.Router();
const https = require('https');
const {AuthFacebook} = require('../controller/authController')
router.post("/auth/facebook",AuthFacebook);
module.exports = router;