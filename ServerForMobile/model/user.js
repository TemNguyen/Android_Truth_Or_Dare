const mongoose = require('mongoose');
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
require('dotenv').config();
const UserSchema = new mongoose.Schema({
    facebookId: String,
    name: String,
})

UserSchema.methods.CreateJWT = function(){
    return jwt.sign({userId: this.facebookId,name:this.name},process.env.JWT_SECRET,{
        expiresIn: process.env.JWT_LIFETIME
    })
}

module.exports = mongoose.model('User', UserSchema);
