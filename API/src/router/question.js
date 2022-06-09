const express = require('express');
const router = express.Router();
const {GetAllQuestion, CreateQuestion, DeleteQuestion,GetQuestion,EditQuestion,ChooseQuestionInPackage} = require('../controller/question');

router.get('/',GetAllQuestion);
router.get('/:id',GetQuestion);
router.get('/:id/Inpackage/:packageId',ChooseQuestionInPackage);
router.post('/',CreateQuestion);
router.patch('/:id',EditQuestion);
router.delete('/:id',DeleteQuestion); 

module.exports = router;