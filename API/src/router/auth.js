const express = require('express');
const router = express.Router();
const {Auth} = require('../controller/authController');

router.get('/auth/:uid',Auth);

module.exports = router;