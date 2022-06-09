const express = require('express');
const router = express.Router();
const {GetAllQuestion, GetQuestion, CreateQuestion, DeleteQuestion,EditQuestion,ChooseQuestionFromPackage} = require('../controller/questionController');
const auth = require('../middleware/authentication');

router.get('/',auth,GetAllQuestion);
router.get('/:id',auth,GetQuestion);
router.post('/',auth,CreateQuestion);
router.patch('/:id',auth,EditQuestion);
router.delete('/:id',auth,DeleteQuestion);
router.get('/package_question/:id_package/:id_question',auth,ChooseQuestionFromPackage);


module.exports = router;