const admin = require('../config/firebase-config');
const db = admin.database();
const Auth = async (req, res) => {
    const uid = req.params.uid;
    const info = await admin.auth().getUser(uid);
    let users;
    const data = {
        firebaseToken: info.uid,
        name: info.displayName
    };
    const checkuser = await db.ref("users").once("value",async (snapshot) => {
        if (snapshot.val() !== null) {
            users = snapshot.val();
        }
    });
    if (users) {
        const keys = Object.keys(users);
        for (let i = 0; i < keys.length; i++) {
            if (users[keys[i]]?.firebaseToken === uid) {
                return res.status(200).json({
                    id:keys[i],
                    name : data.name,
                    firebaseToken: data.firebaseToken});
            }
        }
    }
    const createUser = await db.ref("users").push(data)
    res.status(200).json({
        id:createUser.key,
        name : data.name,
        firebaseToken: data.firebaseToken
    });
}
module.exports = { Auth };