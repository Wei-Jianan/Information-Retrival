from HTMLParser import HTMLParser

from JSONWriter import JSONWriter
from Writable import Writable


class FBParser(HTMLParser, Writable):
    def __init__(self, dir=None):
        # super(MyParser, self).__init__(convert_charrefs=True)
        HTMLParser.__init__(self)
        self.doc = False
        self.docno = False
        self.text = False

        self.writer = JSONWriter(dir)
        self.dic = {}
        # self.counter = 0

    def _writedown(self):
        # print 'writed down by class:', self.__class__, '    file:', self.dic['docno']
        self.writer.dicWrite(self.dic)

    def handle_starttag(self, tag, attrs):
        if tag == 'doc':
            self.doc = True
            self.docno = False
            self.text = False
            # meet 'doc' tag create an empty dictionary
            self.dic = {}
        if tag == 'docno':
            if self.doc:
                self.docno = True
        if tag == 'text':
            if self.doc:
                self.text = True

    def handle_endtag(self, tag):
        if tag == 'doc':
            self.doc = False
            self.docno = False
            self.text = False

            # meet end 'doc' tag to write down a individual file
            self._writedown()
            # print 'dic keys!!!!!!!!', self.dic.keys()
            self.dic.clear()
        if tag == 'docno':
            self.docno = False
            # make sure there is no enter,no space in in dic['docno']
            self.dic['docno'] = ''.join(self.dic['docno'].split())
        if tag == 'text':
            self.text = False

    def handle_data(self, data):
        if self.doc and self.docno:
            self.dic['docno'] = self.dic.get('docno', '') + data
            # print 'Encountered data:'
            # print data
        if self.doc and self.text:
            self.dic['text'] = self.dic.get('text', '') + data
