from HTMLParser import HTMLParser



class FTParser(HTMLParser):
    def __init__(self):
        # super(MyParser, self).__init__(convert_charrefs=True)
        HTMLParser.__init__(self)
        self.doc = False
        self.docno = False
        self.profile = False
        self.date = False
        self.headline = False
        self.text = False
        # self.counter = 0

    def handle_starttag(self, tag, attrs):
        if tag == 'doc':
            self.doc = True
            self.docno = self.docno = self.profile = self.date = self.headline = self.text = False
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
        elif tag == 'docno':
            self.docno = False
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
            print 'docno:\t', data
        elif self.doc and self.profile:
            print 'profile:\t', data
        elif self.doc and self.date:
            print 'date:\t', data
        elif self.doc and self.headline:
            print 'headline:\t', data
        elif self.doc and self.text:
            print 'text:\t', data[0:10]
        # print "Encountered some data  :"#, data
