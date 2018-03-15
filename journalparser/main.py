from sgmllib import SGMLParser
from HTMLParser import HTMLParser


JSON_PATH = './json'
fb_PATH = '/Users/jiananwei/Desktop/temp/fb396001'
fr_PATH = '/Users/jiananwei/Desktop/temp/fr940104.0'
ft_PATH = '/Users/jiananwei/Desktop/temp/ft911_1'
la_PATH = '/Users/jiananwei/Desktop/temp/la010190'

class MyHTMLParser(HTMLParser):
    def handle_starttag(self, tag, attrs):
        print "Encountered a start tag:", tag

    def handle_endtag(self, tag):
        print "Encountered an end tag :", tag

    def handle_data(self, data):
        print "Encountered some data  :", data


def main():
    with open(la_PATH) as f:
        doc = f.read()
        doc = doc.replace('<P>', ' ').replace('</P>', ' ')
        # parser = LAParser()
        # parser.feed(doc)



