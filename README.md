# Courses from Higher School of Economics (Moscow)
This repository provides a storage and reference for all Higher School of Economics  coursework/projects involved in 2019-2020.
Relevant coursera projects written during lockdown are placed here too

## CourseList
7 courses with coding exercises are listed. They are:
- Algorithms part 1 
- Big Data Integration Processing
- Convolutional Neural Network
- How to Win a Data Science Competition: Learn from Top Kagglers
- Advanced Machine Learning
- Social Netowrk Analysis
- Term paper about Graph Neural Network.
Please see below for abstracts for each course.

### 1) Algorithms part 1
This course covers the essential information that every serious programmer needs to know about algorithms and data structures, with emphasis on applications and scientific performance analysis of Java implementations. Part I covers elementary data structures, sorting, and searching algorithms. Coursework are finished in `java` and sorting and searching algorithms like 'Quick sort', 'Merge sort' and 'Balanced searched tree' and 'kd tree' are implemented in `java`.

Link to course: https://www.coursera.org/learn/algorithms-part1

### 2) Big Data Integration Processing
This is a beginner course which aim to
- Describe the connections between data management operations and the big data processing patterns needed to utilize them in large-scale analytical applications
- Identify when a big data problem needs data integration
- Execute simple big data integration and processing on Hadoop and Spark platforms.

I handed in several jupyter notebook from week 3 to week 6, involving very basic tasks of mapreduce and processing data-stream using `pyspark`, `mongodb 4.2` and `python 3.6`

Link to course: https://www.coursera.org/learn/big-data-integration-processing

### 3) Convolutional Neural Network
A classic course designed by Andrew Ng, as an entrance to understand basic CNN throughly, and in order to know how to apply convolutional networks to visual detection and recognition tasks.
Several jupyter notebook are served as homeworks and additional materials for this course.
I used `keras` and `python 3.6` to finish the assignments, including basic car detection by YOLO, neural style transfer (overwhelmed by GANs nowadays) and face recognition  called FaceNet.

Link to course: https://www.coursera.org/learn/convolutional-neural-networks

### 4) How to Win a Data Science Competition: Learn from Top Kagglers
During covid lockdown I took this course , because I would like to get practical experiences from real world data from Kaggle. However there are millions of data sets and each of them have a thousand way to interprete the result. In this course several useful techniques in competitions (and their differences between real world objectives) are intorduced by PhD from HSE and let us practise in the coursework.

In the final assignment, we had been assigned a task on Kaggle to [predict next month sales based on past 30 months sales](https://www.kaggle.com/c/competitive-data-science-predict-future-sales/overview). This is a very intersting and inpsiring task to me as I have never worked at time series data before.
Feature extraction and fine-tuning variables between each model gave me a lot of insights and straighten my thoughts to work in retail business too.

By Jan 2021 my notebook remained at the top 30% of the leaderboard.

Link to course: https://www.coursera.org/learn/competitive-data-science

### 5) Advanced Machine Learning
This is a course by HSE and aim to provide a basic understanding of different regression/classification we can use in `sklearn` and `pandas` using `Python 3.6`.
The main gain from this is to familarize Kaggle environment and understand how natural language could be transformed to features by hashing.

### 6) Social Netowrk Analysis
This is a course by HSE and trying to analyse social network by graphs and different metrics inside.

Further details will be provided.

### 7) Term paper about Graph Neural Network.
This is the code repository corresponding to the term paper I wrote for my first year study - "**Using Graph Neural Networks To Analyse Social Networks**".

I tried to answer a research question: *Whether graph neural networks is a better way to cluster social network users into different groups.*
GNN together with 3 tradiatonal clustering methods are used to cluster users on 3 different graph dataset: *Karate Club*, *Cora* and *fb-CMU-Carnegie49*.
Results looks nice, and it shows that GNN works better than all 3 traditonal methods without fine-tuning.

However, after this paper, several challenges and following studies are planned to be resolved this year as a final year thesis.
1. Bigger datasets are needed (>100k nodes). Data set used in this paper is too small and not representative.
2. Divide big graphs into patches in NN, while maintaining relationships between node and edges.
3. Investigate on more modern techniques, such as GAE and RMSC.

Further updates will be documented in April-May.
