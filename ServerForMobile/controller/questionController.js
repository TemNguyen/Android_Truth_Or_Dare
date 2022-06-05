const Question = require('../model/question');
const Package = require('../model/package_question');
const {StatusCodes} = require('http-status-codes');
const {UnauthenticatedError,BadRequestError,NotFoundError} = require('../errors/index');
const { isValidObjectId } = require('mongoose');
const GetAllQuestion = async (req, res) => {
    const {userId} = req.user;
    const questions = await Question.find({userId: userId});
    res.status(StatusCodes.OK).json({questions,count:questions.length});
}
const GetQuestion = async (req, res) => {
    const {userId} = req.user;
    console.log(userId);
    const {id} = req.params;
    const question = await Question.findOne({
        _id: id,
        userId: userId
    })
    if(!question){
        throw new NotFoundError(`No question with id ${req.params.id}`);
    }
    res.status(StatusCodes.OK).json({question})
}
const CreateQuestion = async (req, res) => {
    const {userId} = req.user;
    const question = await Question.create({
        content: req.body.content,
        packageId: null,
        userId: userId
    })
    res.status(StatusCodes.CREATED).json({question});
}
const EditQuestion = async (req, res) => {
    const {
        body : {content},
        user : {userId},
        params : {id}
    } = req
    if(content == ''){
        throw new BadRequestError('content cannot empty')
    }
    const question = await Question.findByIdAndUpdate({
        _id: id,
        userId: userId
    },{
        content: content,
    },{
        new: true,
        runValidators: true
    })
    if(!question){
        throw new NotFoundError(`No question with id ${req.params.id}`);
    }
    res.status(StatusCodes.OK).json({question})
}
const DeleteQuestion = async (req, res) => {
    const {
        user : {userId},
        params : {id}
    } = req;
    const question = await Question.findByIdAndRemove({
        _id: id,
        userId: userId
    })
    if(!question){
        throw new NotFoundError(`No question with id ${req.params.id}`);
    }
    res.status(StatusCodes.OK).send();
}
const ChooseQuestionFromPackage = async (req,res) => {
    const {
        user : {userId},
        params : {id_package}
    } = req;
    const data = await Question.findOne({packageId: id_package});
    const question = await Question.create({
        content: data.content,
        packageId: null,
        userId: userId
    })
    res.status(StatusCodes.CREATED).json({question});
}
module.exports = {GetAllQuestion, GetQuestion, CreateQuestion, DeleteQuestion,EditQuestion,ChooseQuestionFromPackage}