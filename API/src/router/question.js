const express = require('express');
const router = express.Router();
const {GetAllQuestionFocusRule,GetAllQuestion, CreateQuestion, DeleteQuestion,GetQuestion,EditQuestion,ChooseQuestionInPackage,InstallPackage,GetPackageInstalled} = require('../controller/question');

router.get('/:userId/question',GetAllQuestion);
router.get('/:userId/question/:rule',GetAllQuestionFocusRule);
router.get('/question/:id_question',GetQuestion);
router.get('/:userId/QuestionInPackage/:id_question',ChooseQuestionInPackage);
router.post('/:userId/question',CreateQuestion);
router.patch('/question/:id_question',EditQuestion);
router.delete('/question/:id_question',DeleteQuestion);
router.post('/:userId/package/:packageId',InstallPackage);
router.get('/:userId/packageinstalled',GetPackageInstalled);

module.exports = router;