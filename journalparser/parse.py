import io
import os
import fnmatch

from FBParser import FBParser
from FTParser import FTParser
from LAParser import LAParser

from utils import Utils

def getTasks(dir, pattern):
    file_list = []
    for rootpath, dirnames, filenames in os.walk(dir):
        for filename in fnmatch.filter(filenames, pattern):
            file_list.append(os.path.join(rootpath, filename))
    return file_list

def _parseAndWriteFB(file):
    with io.open(file, mode='r', encoding='utf-8') as f:
        raw_text = f.read()
        parser = FBParser()
        parser.feed(raw_text)




def parseAndWriteFBDir(dir):
    task_list = getTasks(dir, 'fb*')
    # _parseAndWriteFB(task_list[])
    # for task in task_list:
    print task_list[0]
    return 0
    for i in range(1,len(task_list)):
        print task_list[i]
        _parseAndWriteFB(task_list[i])



def main():

    Utils.initialize()
    parseAndWriteFBDir(Utils.DIR_FB)

if __name__ == '__main__':
    main()
