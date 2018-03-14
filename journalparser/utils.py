import os

class Utils():
    DIR_ASSIGNMENT = '/Users/jiananwei/Desktop/Assignment Two'
    DIR_JSON = '/Users/jiananwei/Desktop/JSON'

    @classmethod
    def initialize(cls, root_dir = None, json_dir = None):
        if root_dir:
            cls.DIR_ASSIGNMENT = root_dir
        if json_dir:
            cls.DIR_JSON = json_dir
        if not os.path.isdir(cls.DIR_JSON):
            os.mkdir(cls.DIR_JSON)
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