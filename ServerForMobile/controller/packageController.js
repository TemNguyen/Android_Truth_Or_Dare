const Package = require('../model/package_question');
const Question = require('../model/question');
const {StatusCodes} = require('http-status-codes');
const {UnauthenticatedError,BadRequestError,NotFoundError} = require('../errors/index');
const GetAllPackage = async (req, res, next) => {
    const package = await Package.find({})
    res.status(StatusCodes.OK).json({package});
}
const GetPackage = async (req, res, next) => {
    const id = req.params.id;
    const package = await Package.findOne({_id: id});
    res.status(StatusCodes.OK).json({package});
}
const CreatePackage = async (req, res, next) => {
    const package = await Package.create(req.body);
    res.status(StatusCodes.CREATED).json({package});
}
const DeletePackage = async (req, res, next) => {
    const package = await Package.findByIdAndRemove({
        _id: req.params.id,
    })
    if(!package){
        throw new NotFoundError(`No package with id ${req.params.id}`);
    }
    const question = await Question.deleteMany({packageId: req.params.id});
    res.status(StatusCodes.OK).send();
}
const EditPackage = async (req, res, next) => {
    const {name} = req.body;
    const package = await Package.findByIdAndUpdate({_id:req.params.id},{
        name:name
    },{
        new: true,
        runValidators: true
    })
    if(!package){
        throw new NotFoundError(`No package with id ${req.params.id}`);
    }
    res.status(StatusCodes.OK).json({package});
}
const CreateQuestionForPackage = async (req, res, next) => {
    const question = await Question.create({
        content: req.body.content,
        packageId: req.params.id,
        userId: null
    })
    res.status(StatusCodes.CREATED).json({question});
}
const GetQuestionInPackage = async (req, res, next) => {
    const questions = await Question.find({packageId: req.params.id});
    res.status(StatusCodes.OK).json({questions});
}
module.exports = {GetAllPackage, GetPackage, CreatePackage, DeletePackage, EditPackage,CreateQuestionForPackage,GetQuestionInPackage}