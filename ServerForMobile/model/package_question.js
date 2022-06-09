const mongoose = require('mongoose');

const PackageSchema = new mongoose.Schema({
    name:{
        type: String,
        required: [true,'please provide content'],
        minLength:3,
        maxLength:255
    }
})

module.exports = mongoose.model("Package",PackageSchema);