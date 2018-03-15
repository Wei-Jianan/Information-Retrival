from HTMLParser import HTMLParser

from journalparser.JSONWriter import JSONWriter
from journalparser.Writable import Writable


class FTParser(HTMLParser, Writable):
    def __init__(self, dir=None):
        # super(MyParser, self).__init__(convert_charrefs=True)
        HTMLParser.__init__(self)
        self.doc = False
        self.docno = False
        self.profile = False
        self.date = False
        self.headline = False
        self.text = False
        # self.counter = 0
        self.writer = JSONWriter(dir)
        self.dic = {}

    def _writedown(self):
        # print 'writed down by class:', self.__class__, '    file:', self.dic['docno']
        self.writer.dicWrite(self.dic)

    def handle_starttag(self, tag, attrs):
        if tag == 'doc':
            self.doc = True
            self.docno = self.docno = self.profile = self.date = self.headline = self.text = False
            # meet 'doc' tag create an empty dictionary
            self.dic = {}
        elif tag == 'docno':
            self.docno = True
        elif tag == 'profile':
            self.profile = True
        elif tag == 'date':
            self.date = True
        elif tag =='headline':
            self.headline = True
        elif tag == 'text':
            self.text = True
        # print "Encountered a start tag:", tag

    def handle_endtag(self, tag):
        if tag == 'doc':
            self.doc = False
            self.docno = self.docno = self.profile = self.date = self.headline = self.text = False
            # meet end 'doc' tag to write down a individual file
            self._writedown()
            # print 'dic keys!!!!!!!!', self.dic.keys()
            self.dic.clear()
        elif tag == 'docno':
            self.docno = False
            # make sure there is no enter,no space in in dic['docno']
            self.dic['docno'] = ''.join(self.dic['docno'].split())
        elif tag == 'profile':
            self.profile = False
        elif tag == 'date':
            self.date = False
        elif tag =='headline':
            self.headline = False
        elif tag == 'text':
            self.text = False
        # print "Encountered an end tag :", tag

    def handle_data(self, data):
        if self.doc and self.docno:
            self.dic['docno'] = self.dic.get('docno', '') + data
        elif self.doc and self.profile:
            self.dic['profile'] = self.dic.get('profile', '') + data
        elif self.doc and self.date:
            self.dic['date'] = self.dic.get('date', '') + data
        elif self.doc and self.headline:
            self.dic['headline'] = self.dic.get('headline', '') + data
        elif self.doc and self.text:
            self.dic['text'] = self.dic.get('text', '') + data
        # print "Encountered some data  :"#, data
