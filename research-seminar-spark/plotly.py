import os
import re
from operator import add
import string
import operator
import pandas as pd

import plotly
import plotly.express as px

from pyspark import SparkConf, SparkContext, SQLContext
from pyspark.sql.functions import *

import datetime

datafile_json = "trump_tweet_all.json"

def get_keyval(row):
    timestamp = row.timestamp
    return [datetime.datetime(timestamp.year, timestamp.month, 1), 1]

def get_word_frequency(df, keyword):

    mapped_rdd = df.rdd.map (lambda row: get_keyval(row))
    counts_rdd = mapped_rdd.reduceByKey(add)
    single_word_count = counts_rdd.sortBy(lambda a: a[0], False).collect()

    df_date = pd.DataFrame(single_word_count, columns=['Datetime', 'Count'])
    df_date ["Keyword"] =keyword
    print(df_date)
    return df_date

def process_json(abspath, sparkcontext):

    # Create an sql context so that we can query data files in sql like syntax
    sqlContext = SQLContext (sparkcontext)

    #get timetrend based on tweets no.
    df = sqlContext.read.json(os.path.join (
        abspath, datafile_json)).select(to_timestamp(col("created_at"), "EEE MMM dd HH:mm:ss +0000 yyyy").alias('timestamp'), col("text").alias("tweet"))

    #dfAmerica = df2.filter(col("text").contains("great"))
    #get_word_frequency(dfAmerica)

    dfAmerica = df.filter(lower(col("text")).contains("america"))
    df_date = get_word_frequency(dfAmerica, "America")
    dfChina = df.filter(lower(col("text")).contains("china"))
    df_date = df_date.append(get_word_frequency(dfChina, "China"))
    dfGreat = df.filter(lower(col("text")).contains("great"))
    df_date = df_date.append(get_word_frequency(dfGreat, "Great"))
    print("df date: ")
    print(df_date)
    #plot
    fig = px.line(df_date, x='Datetime', y='Count', color="Keyword", title='No. of tweets with the words "America", "China" and "Great"')
    fig.update_xaxes(rangeslider_visible=True, title_text = "Time")
    fig.update_yaxes(title_text='Tweet Count')
    plotly.offline.plot(fig)

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