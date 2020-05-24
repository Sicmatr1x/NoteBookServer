
## install

### MongoDB

```
use NoteBookServer
db.createUser({user:'NoteBookServer_appln',pwd:'Friday5',roles:[{role:'readWrite',db:'NoteBookServer'}]})
```