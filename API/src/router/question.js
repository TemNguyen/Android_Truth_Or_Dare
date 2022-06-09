const express = require('express');
const router = express.Router();
const {GetAllQuestion, CreateQuestion, DeleteQuestion,GetQuestion,EditQuestion} = require('../controller/question');

router.get('/',GetAllQuestion);
router.get('/:id',GetQuestion);
router.post('/',CreateQuestion);
router.patch('/:id',EditQuestion);
router.delete('/:id',DeleteQuestion);

module.exports = router;