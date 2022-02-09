package com.microservices.demo.twitter.to.kafka.service.runner.impl;

import com.microservices.demo.kafka.producer.config.TwitterToKafkaServiceConfigData;
import com.microservices.demo.twitter.to.kafka.service.exception.TwitterKafkaServiceException;
import com.microservices.demo.twitter.to.kafka.service.listener.TwitterKafkaStatusListener;
import com.microservices.demo.twitter.to.kafka.service.runner.StreamRunner;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.config.ConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

@Component
@ConditionalOnProperty(name = "twitter-to-kafka-service.enable-mock-tweets", havingValue = "true")
public class MockKafkaStreamRunner implements StreamRunner {

    private static final Logger LOG = LoggerFactory.getLogger(MockKafkaStreamRunner.class);

    private final TwitterToKafkaServiceConfigData twitterToKafkaServiceConfigData;
    private final TwitterKafkaStatusListener twitterKafkaStatusListener;

    private static final Random RANDOM = new Random();
    private static final String[] WORDS = new String[]{
            "Lorem",
            "ipsum",
            "dolor",
            "sit",
            "amet",
            "consectetur",
            "adipiscing",
            "elit.",
            "Aenean",
            "ut",
            "nibh",
            "posuere,",
            "ultrices",
            "dui",
            "quis",
            "eleifend",
            "mauris",
            "Nam",
            "pretium",
            "est",
            "et",
            "tempus",
            "aliquet",
            "Phasellus",
            "elementum",
            "eleifend",
            "magna",
            "nec",
            "tincidunt",
            "Proin"
    };

    private static final String tweetAsRawJson = "{" +
            "\"created_at\":\"{0}\"," +
            "\"id\":\"{1}\"," +
            "\"text\":\"{2}\"," +
            "\"user\":{\"id\":\"{3}\"}" +
            "}";

    private static final String TWITTER_STATUS_DATE_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy";

    public MockKafkaStreamRunner(TwitterToKafkaServiceConfigData configData, TwitterKafkaStatusListener listener) {
        this.twitterToKafkaServiceConfigData = configData;
        this.twitterKafkaStatusListener = listener;
    }

    @Override
    public void start() throws TwitterException {
        String[] keywords = twitterToKafkaServiceConfigData.getTwitterKeywords().toArray(new String[0]);
        Integer mockMinTweetLength = twitterToKafkaServiceConfigData.getMockMinTweetLength();
        Integer mockMaxTweetLength = twitterToKafkaServiceConfigData.getMockMaxTweetLength();
        Long mockSleepMs = twitterToKafkaServiceConfigData.getMockSleepMs();
        LOG.info("Starting mock filtering tweet streams for keywords {}", Arrays.toString(keywords));
        simulateTwitterStream(keywords, mockMinTweetLength, mockMaxTweetLength, mockSleepMs);
    }

    private void simulateTwitterStream(String[] keywords, Integer mockMinTweetLength, Integer mockMaxTweetLength, Long mockSleepMs) {
        //run in a new single thread and not in the main thread
        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                while (true) {
                    LOG.info("Creating tweet on separated thread ...");
                    String formattedTweetAsRawJson = getFormattedTweet(keywords, mockMinTweetLength, mockMaxTweetLength);
                    Status status = TwitterObjectFactory.createStatus(formattedTweetAsRawJson);
                    twitterKafkaStatusListener.onStatus(status);
                    sleep(mockSleepMs);
                }
            } catch (TwitterException e) {
                LOG.error("Error creating twitter status!", e);
            } catch (ConfigException configException) {
                LOG.error("Error during configuration!", configException);
            }
        });
/*        for (int i = 0; i <5; i++){
            try {
                LOG.info("Creating tweet number {} ..." , i);
                String formattedTweetAsRawJson = getFormattedTweet(keywords, mockMinTweetLength, mockMaxTweetLength);
                Status status = TwitterObjectFactory.createStatus(formattedTweetAsRawJson);
                twitterKafkaStatusListener.onStatus(status);
            } catch (TwitterException e) {
                LOG.error("Error creating twitter status!", e);
            } catch (ConfigException configException) {
                LOG.error("Error creating twitter status!", configException);
            }
        }*/
    }

    private void sleep(Long mockSleepMs) {
        try {
            Thread.sleep(mockSleepMs);
        } catch (InterruptedException e) {
            throw new TwitterKafkaServiceException("Error while sleeping for waiting new status create!!");
        }
    }

    private String getFormattedTweet(String[] keywords, Integer mockMinTweetLength, Integer mockMaxTweetLength) {
        String[] params = new String[]{
                ZonedDateTime.now().format(DateTimeFormatter.ofPattern(TWITTER_STATUS_DATE_FORMAT, Locale.ENGLISH)),
                String.valueOf(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE)),
                getRandomTweetContenr(keywords, mockMinTweetLength, mockMaxTweetLength),
                String.valueOf(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE))
        };

        return formatTweetAsJsonWithParams(params);
    }

    private String formatTweetAsJsonWithParams(String[] params) {
        String tweet = tweetAsRawJson;
        for (int i = 0; i < params.length; i++) {
            tweet = tweet.replace("{" + i + "}", params[i]);
        }
        return tweet;
    }

    private String getRandomTweetContenr(String[] keywords, Integer mockMinTweetLength, Integer mockMaxTweetLength) {
        StringBuilder tweet = new StringBuilder();
        int tweetLength = RANDOM.nextInt(mockMaxTweetLength - mockMinTweetLength + 1) + mockMinTweetLength;
        return constructRandomTweet(keywords, tweet, tweetLength);
    }

    private String constructRandomTweet(String[] keywords, StringBuilder tweet, int tweetLength) {
        for (int i = 0; i < tweetLength; i++) {
            tweet.append(WORDS[RANDOM.nextInt(WORDS.length)]).append(" ");
            if (i == tweetLength / 2) {
                tweet.append(keywords[RANDOM.nextInt(keywords.length)]).append(" ");
            }
        }
        return tweet.toString().trim();
    }
}
