from journalparser import utils
import json
import os

class JSONWriter():

    def __init__(self, dir=None):
        self.dir = dir
        if not dir:
            self.dir = utils.Utils.DIR_JSON
    def dicWrite(self, dic):
        filename = dic['docno'] + '.json'
        json_byte = json.dumps(dic)
        with open(os.path.join(self.dir, filename), 'wb') as f:
            f.write(json_byte)


# dic = {'docno':'1111', 'sdf':'123','asf':'123'}
# writer = JSONWriter()
# writer.dicWrite(dic)
