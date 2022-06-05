const express = require('express');
const router = express.Router();
const {GetAllPackage, GetPackage, CreatePackage, DeletePackage, EditPackage,CreateQuestionForPackage} = require('../controller/packageController');

router.get('/',GetAllPackage);
router.get('/:id',GetPackage);
router.post('/',CreatePackage);
router.patch('/:id',EditPackage);
router.delete('/:id',DeletePackage);
router.post('/:id/createquestion',CreateQuestionForPackage);

module.exports = router;