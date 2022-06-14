const express = require('express');
const router = express.Router();
const {GetAllPackages,GetPackage,GetQuestionInPackage,CreateQuestionForPackage,CreatePackage,EditPackage,DeletePackage } = require('../controller/package');

router.get('/',GetAllPackages);
router.get('/:id',GetPackage);
router.get('/:id/question',GetQuestionInPackage);
router.post('/:id/question/create',CreateQuestionForPackage);
router.post('/',CreatePackage);
router.patch('/:id',EditPackage);
router.delete('/:id',DeletePackage);

module.exports = router;