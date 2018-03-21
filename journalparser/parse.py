import argparse
import io
import multiprocessing
import os
import fnmatch
from multiprocessing import Pool
from multiprocessing.pool import ThreadPool

from FBParser import FBParser
from FTParser import FTParser
from LAParser import LAParser
from FRParser import FRParser
import functools

from utils import Utils


def _getTasks(dir, pattern):
    file_list = []
    for rootpath, dirnames, filenames in os.walk(dir):
        for filename in fnmatch.filter(filenames, pattern):
            file_list.append(os.path.join(rootpath, filename))
    return file_list


def _timeit(func):
    @functools.wraps(func)
    def wrapper(*args, **kwds):
        import time
        start = time.time()
        result = func(*args, **kwds)
        end = time.time()
        print 'task', func.__name__, '\ttake\t', end - start, ' seconds'

    return wrapper


def _parseAndWriteFB(file):
    with io.open(file, mode='r', encoding='latin-1') as f:
        # with io.open(file, mode='r', encoding='utf-8') as f:
        raw_text = f.read()

        parser = FBParser()
        parser.feed(raw_text)


@_timeit
def parseAndWriteFBDir(dir):
    task_list = _getTasks(dir, 'fb*')
    print 'there are ', len(task_list), 'tasks in ', dir
    for task in task_list:
        # print task
        _parseAndWriteFB(task)


def _parseAndWriteFT(file):
    with io.open(file, mode='r', encoding='latin-1') as f:
        # with io.open(file, mode='r', encoding='utf-8') as f:
        raw_text = f.read()
        parser = FTParser()
        parser.feed(raw_text)


@_timeit
def parseAndWriteFTDir(dir):
    task_list = _getTasks(dir, 'ft*')
    print 'there are ', len(task_list), 'tasks in ', dir
    for task in task_list:
        # print task
        _parseAndWriteFT(task)


def _parseAndWriteLA(file):
    with io.open(file, mode='r', encoding='latin-1') as f:
        # with io.open(file, mode='r', encoding='utf-8') as f:
        raw_text = f.read()
        # attention this is important, and tricky!!
        filter_text = raw_text.replace('<P>', ' ').replace('</P>', ' ')
        parser = LAParser()
        parser.feed(filter_text)


@_timeit
def parseAndWriteLADir(dir):
    task_list = _getTasks(dir, 'la*')
    print 'there are ', len(task_list), 'tasks in ', dir
    for task in task_list:
        # print task
        _parseAndWriteLA(task)


def _parseAndWriteFR(file):
    # print "opening FR file", file
    with open(file, mode='r') as f:
        # with io.open(file, mode='r', encoding='utf-8') as f:


        raw_text = f.read()
        #  delete all lines starting with '<!--'
        filter_text = str()
        for line in raw_text.splitlines():
            if not line.startswith('<!--'):
                filter_text += line

        parser = FRParser()
        parser.feed(filter_text)



@_timeit
def parseAndWriteFRDir(dir):
    task_list = _getTasks(dir, 'fr*')
    print 'there are ', len(task_list), 'tasks in ', dir

    # only first file
    # _parseAndWriteFR(task_list[0])

    for task in task_list:
        _parseAndWriteFR(task)


@_timeit
def parseMultiProc():
    print "number of processors: ", multiprocessing.cpu_count()
    pool = Pool(multiprocessing.cpu_count())
    pool.apply_async(parseAndWriteFBDir, (Utils.DIR_FB,))
    pool.apply_async(parseAndWriteFTDir, (Utils.DIR_FT,))
    pool.apply_async(parseAndWriteLADir, (Utils.DIR_LA,))
    pool.apply_async(parseAndWriteFRDir, (Utils.DIR_FR,))
    # result = pool.apply_async(func = sleep, args = (5, ))

    pool.close()
    pool.join()
    print "all done"


@_timeit
def parseMultiThread():
    print "number of processors: ", multiprocessing.cpu_count()
    pool = ThreadPool(multiprocessing.cpu_count())
    # pool.apply_async(parseAndWriteFBDir, (Utils.DIR_FB,))
    # pool.apply_async(parseAndWriteFTDir, (Utils.DIR_FT,))
    # pool.apply_async(parseAndWriteLADir, (Utils.DIR_LA,))
    pool.apply_async(parseAndWriteFRDir, (Utils.DIR_FR,))
    # result = pool.apply_async(func = sleep, args = (5, ))

    pool.close()
    pool.join()
    print "all done"


@_timeit
def parseInOrder():
    parseAndWriteFBDir(Utils.DIR_FB)
    parseAndWriteFTDir(Utils.DIR_FT)
    parseAndWriteLADir(Utils.DIR_LA)
    print 'all done'


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('-f', type=str,
                        help="a full path to Assigment folder.\t Attention there is a space in 'Assignment Two'")
    parser.add_argument('-t', type=str, help='a full path where you want to save the json files')
    args = parser.parse_args()

    if args.t and args.f:
        Utils.initialize(args.f, args.t)
        parseMultiProc()
        # parseMultiThread()
    else:
        print "-f are -t are required\ncheck python2 parser.py --help"
    # print parser.parse_args(('from'))
