import os
import re
from operator import add
import string
import nltk
nltk.download('stopwords')
import operator
import pandas as pd

import plotly
import plotly.express as px

from pyspark import SparkConf, SparkContext, SQLContext
from pyspark.sql.functions import *

import time

datafile_json = "trump_tweet_all.json"
datafile_csv = ""
datafile_psv = ""


def remove_punct(text):
    #remove puntuation
    text  = "".join([char for char in text if char not in string.punctuation])
    #remove all numbers
    text = re.sub('[0-9]+', '', text)
    return text
def get_additional_stopwords():
    stopwords = ['a', 'about', 'above', 'across', 'after', 'afterwards']
    stopwords += ['again', 'against', 'all', 'almost', 'alone', 'along']
    stopwords += ['already', 'also', 'although', 'always', 'am', 'among']
    stopwords += ['amongst', 'amoungst', 'amount', 'an', 'and', 'another']
    stopwords += ['any', 'anyhow', 'anyone', 'anything', 'anyway', 'anywhere']
    stopwords += ['are', 'around', 'as', 'at', 'back', 'be', 'became']
    stopwords += ['because', 'become', 'becomes', 'becoming', 'been']
    stopwords += ['before', 'beforehand', 'behind', 'being', 'below']
    stopwords += ['beside', 'besides', 'between', 'beyond', 'bill', 'both']
    stopwords += ['bottom', 'but', 'by', 'call', 'can', 'cannot', 'cant']
    stopwords += ['co', 'computer', 'con', 'could', 'couldnt', 'cry', 'de']
    stopwords += ['describe', 'detail', 'did', 'do', 'done', 'down', 'due']
    stopwords += ['during', 'each', 'eg', 'eight', 'either', 'eleven', 'else']
    stopwords += ['elsewhere', 'empty', 'enough', 'etc', 'even', 'ever']
    stopwords += ['every', 'everyone', 'everything', 'everywhere', 'except']
    stopwords += ['few', 'fifteen', 'fifty', 'fill', 'find', 'fire', 'first']
    stopwords += ['five', 'for', 'former', 'formerly', 'forty', 'found']
    stopwords += ['four', 'from', 'front', 'full', 'further', 'get', 'give']
    stopwords += ['go', 'had', 'has', 'hasnt', 'have', 'he', 'hence', 'her']
    stopwords += ['here', 'hereafter', 'hereby', 'herein', 'hereupon', 'hers']
    stopwords += ['herself', 'him', 'himself', 'his', 'how', 'however']
    stopwords += ['hundred', 'i', 'ie', 'if', 'in', 'inc', 'indeed']
    stopwords += ['interest', 'into', 'is', 'it', 'its', 'itself', 'keep']
    stopwords += ['last', 'latter', 'latterly', 'least', 'less', 'ltd', 'made']
    stopwords += ['many', 'may', 'me', 'meanwhile', 'might', 'mill', 'mine']
    stopwords += ['more', 'moreover', 'most', 'mostly', 'move', 'much']
    stopwords += ['must', 'my', 'myself', 'name', 'namely', 'neither', 'never']
    stopwords += ['nevertheless', 'next', 'nine', 'no', 'nobody', 'none']
    stopwords += ['noone', 'nor', 'not', 'nothing', 'now', 'nowhere', 'of']
    stopwords += ['off', 'often', 'on','once', 'one', 'only', 'onto', 'or']
    stopwords += ['other', 'others', 'otherwise', 'our', 'ours', 'ourselves']
    stopwords += ['out', 'over', 'own', 'part', 'per', 'perhaps', 'please']
    stopwords += ['put', 'rather', 're', 's', 'same', 'see', 'seem', 'seemed']
    stopwords += ['seeming', 'seems', 'serious', 'several', 'she', 'should']
    stopwords += ['show', 'side', 'since', 'sincere', 'six', 'sixty', 'so']
    stopwords += ['some', 'somehow', 'someone', 'something', 'sometime']
    stopwords += ['sometimes', 'somewhere', 'still', 'such', 'system', 'take']
    stopwords += ['ten', 'than', 'that', 'the', 'their', 'them', 'themselves']
    stopwords += ['then', 'thence', 'there', 'thereafter', 'thereby']
    stopwords += ['therefore', 'therein', 'thereupon', 'these', 'they']
    stopwords += ['thick', 'thin', 'third', 'this', 'those', 'though', 'three']
    stopwords += ['three', 'through', 'throughout', 'thru', 'thus', 'to']
    stopwords += ['together', 'too', 'top', 'toward', 'towards', 'twelve']
    stopwords += ['twenty', 'two', 'un', 'under', 'until', 'up', 'upon']
    stopwords += ['us', 'very', 'via', 'was', 'we', 'well', 'were', 'what']
    stopwords += ['whatever', 'when', 'whence', 'whenever', 'where']
    stopwords += ['whereafter', 'whereas', 'whereby', 'wherein', 'whereupon']
    stopwords += ['wherever', 'whether', 'which', 'while', 'whither', 'who']
    stopwords += ['whoever', 'whole', 'whom', 'whose', 'why', 'will', 'with']
    stopwords += ['within', 'without', 'would', 'yet', 'you', 'your']
    stopwords += ['yours', 'yourself', 'yourselves']
    return stopwords
def get_keyval(row):

    # get the text from the row entry
    text = row.text

    # lower case text and split by space to get the words
    words = remove_punct(text).replace('\n', '').lower().split (" ")
    # filter words which appear very often in English which make noise
    stopword = nltk.corpus.stopwords.words('english')
    stopword.extend(['realdonaldtrump', 'rt', 'amp', 'us', 'would', 'get'])
    stopword.extend(get_additional_stopwords())
    # for each word, send back a count of 1
    return [[w, 1] for w in words if w not in stopword]

def gen_bar_chart(word_count):
    df = pd.DataFrame(word_count, columns=['Word', 'Count'])
    fig = px.bar(df, x='Word' ,y='Count', color='Count', labels={'Count':'Word Count of tweets'})
    
    plotly.offline.plot(fig)

def get_counts(df):
    # just for the heck of it, show 2 results without truncating the fields
    df.show (2, False)
    # for each text entry, get it into tokens and assign a count of 1
    # we need to use flat map because we are going from 1 entry to many
    mapped_rdd = df.rdd.flatMap (lambda row: get_keyval(row))
    # for each identical token (i.e. key) add the counts
    # this gets the counts of each word
    counts_rdd = mapped_rdd.reduceByKey(add)
    #sort it
    sorted_counts_rdd = counts_rdd.sortBy(lambda a: a[1], False)
    # get the final output into a list
    word_count = sorted_counts_rdd.collect()

    # print the counts
    # for e in word_count[:100]:
    #     print (e)

    #generate barchart
    gen_bar_chart(word_count[1:50])
    # save to a csv

def process_json(abspath, sparkcontext):

    # Create an sql context so that we can query data files in sql like syntax
    sqlContext = SQLContext (sparkcontext)

    ##1
    # read the json data file and select only the field labeled as "text"
    # this returns a spark data frame
    df = sqlContext.read.json (os.path.join (
        abspath, datafile_json)).select ("text")
    ## use the data frame to get counts of the text field
    get_counts(df)

def process_csv(abspath, sparkcontext):

    # Create an sql context so that we can query data files in sql like syntax
    sqlContext = SQLContext (sparkcontext)

    # read the CSV data file and select only the field labeled as "text"
    # this returns a spark data frame
    df = sqlContext.read.load (os.path.join (abspath, datafile_csv),
                               format='com.databricks.spark.csv',
                               header='true',
                               inferSchema='true').select ("text")

    # use the data frame to get counts of the text field
    get_counts (df)


def process_psv(abspath, sparkcontext):
    """Process the pipe separated file"""

    # return an rdd of the tsv file
    rdd = sparkcontext.textFile (os.path.join (abspath, datafile_psv))

    # we only want the "text" field, so return only item[1] after
    # tokenizing by pipe symbol.
    text_field_rdd = rdd.map (lambda line: [re.split ('\|', line)[1]])

    # create a data frame from the above rdd
    # label the column as 'text'
    df = text_field_rdd.toDF (["text"])

    # use the data frame to get counts of the text field
    get_counts (df)


if __name__ == "__main__":

    # absolute path to this file
    abspath = os.path.abspath(os.path.dirname(__file__))

    # Create a spark configuration with 20 threads.
    # This code will run locally on master
    conf = (SparkConf ()
            . setMaster("local[20]")
            . setAppName("sample app for reading files")
            . set("spark.executor.memory", "2g"))

    sc = SparkContext(conf=conf)

    # process the json data file
    process_json (abspath, sc)
#4. visualize.