import os

class Utils():
    DIR_ASSIGNMENT = None #'/users/jiananwei/desktop/assignment two'
    DIR_JSON = None #'/Users/jiananwei/Desktop/JSON'

    @classmethod
    def initialize(cls, root_dir = None, json_dir = None):
        print 'initialized'
        if root_dir:
            cls.DIR_ASSIGNMENT = os.path.abspath(root_dir)
        if json_dir:
            cls.DIR_JSON = os.path.abspath(json_dir)
        if not os.path.isdir(cls.DIR_JSON):
            os.mkdir(cls.DIR_JSON)
            print 'JSON dir created'
        else:
            print 'JSON alread exixted'
        cls.DIR_FB = os.path.join(cls.DIR_ASSIGNMENT, 'fbis')
        cls.DIR_FT = os.path.join(cls.DIR_ASSIGNMENT, 'ft')
        cls.DIR_FR = os.path.join(cls.DIR_ASSIGNMENT, 'fr94')
        cls.DIR_LA = os.path.join(cls.DIR_ASSIGNMENT, 'latimes')





if __name__ == '__main__':
    # Utils.initialize()
    # print Utils.DIR_FB
    # for a, b, c in os.walk(Utils.DIR_LA):
    #     print c
    pass