from HTMLParser import HTMLParser


class LAParser(HTMLParser):
    def __init__(self):
        # super(MyParser, self).__init__(convert_charrefs=True)
        HTMLParser.__init__(self)
        self.doc = False
        self.docno = False
        self.date = False
        self.section = False
        self.headline = False
        self.text = False
        # self.counter = 0

    def handle_starttag(self, tag, attrs):
        if tag == 'doc':
            self.doc = True
            self.docno = self.date = self.section = self.headline = self.text = False
        elif tag == 'docno':
            self.docno = True
        elif tag == 'date':
            self.date = True
        elif tag == 'section':
            self.section = True
        elif tag == 'headline':
            self.headline = True
        elif tag == 'text':
            self.text = True
        # print "Encountered an end tag :", tag

    def handle_endtag(self, tag):
        if tag == 'doc':
            self.doc = False
            self.docno = self.date = self.section = self.headline = self.text = False
        elif tag == 'docno':
            self.docno = False
        elif tag == 'date':
            self.date = False
        elif tag == 'section':
            self.section = False
        elif tag == 'headline':
            self.headline = False
        elif tag == 'text':
            self.text = False

        # print "Encountered an end tag :", tag
    def handle_data(self, data):
        if self.doc and self.docno:
            print 'docno', data if len(data) <= 20 else data[:19]
        elif self.doc and self.date:
            print 'date', data if len(data) <= 20 else data[:19]
        elif self.doc and self.section:
            print 'section', data if len(data) <= 20 else data[:19]
        elif self.doc and self.headline:
            print 'headline', data if len(data) <= 20 else data[:19]
        elif self.doc and self.text:
            print 'text', data if len(data) <= 20 else data[:19]