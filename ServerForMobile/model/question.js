const mongoose = require('mongoose');

const QuestionSchema = new mongoose.Schema({
    content:{
        type: String,
        required: [true,'please provide content'],
        minLength:10,
        maxLength:255
    },
    packageId:{
        type: String
    },
    userId:{
        type: String
    }
})

module.exports = mongoose.model("Question",QuestionSchema);