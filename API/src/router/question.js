const express = require('express');
const router = express.Router();
const {GetAllQuestion, CreateQuestion, DeleteQuestion,GetQuestion,EditQuestion,ChooseQuestionInPackage,InstallPackage,GetPackageInstalled} = require('../controller/question');

router.get('/:userId/question',GetAllQuestion);
router.get('/:userId/question/:id_question',GetQuestion);
router.get('/:userId/QuestionInPackage/:id_question',ChooseQuestionInPackage);
router.post('/:userId/question',CreateQuestion);
router.patch('/:userId/question/:id_question',EditQuestion);
router.delete('/:userId/question/:id_question',DeleteQuestion);
router.get('/:userId/package/:packageId',InstallPackage);
router.get('/:userId/packageinstalled',GetPackageInstalled);

module.exports = router;