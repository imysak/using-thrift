package com.imysak.thrift.commons;

import org.apache.commons.pool.impl.GenericObjectPool;

/**
 * Helper class for easy spring config of GenericObjectPool settings.
 * @author team ProdTastic
 * 
 */
public class PoolConfig extends GenericObjectPool.Config {

    public PoolConfig() {
        setTestOnBorrow(true);
    }
    /**
     * @return the maxIdle
     */
    public int getMaxIdle() {
        return maxIdle;
    }
    /**
     * @param maxIdle the maxIdle to set
     */
    public void setMaxIdle(final int maxIdle) {
        this.maxIdle = maxIdle;
    }
    /**
     * @return the minIdle
     */
    public int getMinIdle() {
        return minIdle;
    }
    /**
     * @param minIdle the minIdle to set
     */
    public void setMinIdle(final int minIdle) {
        this.minIdle = minIdle;
    }
    /**
     * @return the maxActive
     */
    public int getMaxActive() {
        return maxActive;
    }
    /**
     * @param maxActive the maxActive to set
     */
    public void setMaxActive(final int maxActive) {
        this.maxActive = maxActive;
    }
    /**
     * @return the maxWait
     */
    public long getMaxWait() {
        return maxWait;
    }
    /**
     * @param maxWait the maxWait to set
     */
    public void setMaxWait(final long maxWait) {
        this.maxWait = maxWait;
    }
    /**
     * @return the whenExhaustedAction
     */
    public byte getWhenExhaustedAction() {
        return whenExhaustedAction;
    }
    /**
     * @param whenExhaustedAction the whenExhaustedAction to set
     */
    public void setWhenExhaustedAction(final byte whenExhaustedAction) {
        this.whenExhaustedAction = whenExhaustedAction;
    }
    /**
     * @return the testOnBorrow
     */
    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }
    /**
     * @param testOnBorrow the testOnBorrow to set
     */
    public void setTestOnBorrow(final boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }
    /**
     * @return the testOnReturn
     */
    public boolean isTestOnReturn() {
        return testOnReturn;
    }
    /**
     * @param testOnReturn the testOnReturn to set
     */
    public void setTestOnReturn(final boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }
    /**
     * @return the testWhileIdle
     */
    public boolean isTestWhileIdle() {
        return testWhileIdle;
    }
    /**
     * @param testWhileIdle the testWhileIdle to set
     */
    public void setTestWhileIdle(final boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }
    /**
     * @return the timeBetweenEvictionRunsMillis
     */
    public long getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }
    /**
     * @param timeBetweenEvictionRunsMillis the timeBetweenEvictionRunsMillis to set
     */
    public void setTimeBetweenEvictionRunsMillis(final long timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }
    /**
     * @return the numTestsPerEvictionRun
     */
    public int getNumTestsPerEvictionRun() {
        return numTestsPerEvictionRun;
    }
    /**
     * @param numTestsPerEvictionRun the numTestsPerEvictionRun to set
     */
    public void setNumTestsPerEvictionRun(final int numTestsPerEvictionRun) {
        this.numTestsPerEvictionRun = numTestsPerEvictionRun;
    }
    /**
     * @return the minEvictableIdleTimeMillis
     */
    public long getMinEvictableIdleTimeMillis() {
        return minEvictableIdleTimeMillis;
    }
    /**
     * @param minEvictableIdleTimeMillis the minEvictableIdleTimeMillis to set
     */
    public void setMinEvictableIdleTimeMillis(final long minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }
    /**
     * @return the softMinEvictableIdleTimeMillis
     */
    public long getSoftMinEvictableIdleTimeMillis() {
        return softMinEvictableIdleTimeMillis;
    }
    /**
     * @param softMinEvictableIdleTimeMillis the softMinEvictableIdleTimeMillis to set
     */
    public void setSoftMinEvictableIdleTimeMillis(final long softMinEvictableIdleTimeMillis) {
        this.softMinEvictableIdleTimeMillis = softMinEvictableIdleTimeMillis;
    }

}
