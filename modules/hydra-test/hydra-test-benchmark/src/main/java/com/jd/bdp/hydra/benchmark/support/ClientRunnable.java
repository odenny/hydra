/**
 * nfs-rpc
 *   Apache License
 *   
 *   http://code.google.com/p/nfs-rpc (c) 2011
 */
package com.jd.bdp.hydra.benchmark.support;

import java.util.List;

/**
 * client runnable,so we can collect results
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public interface ClientRunnable extends Runnable {
	
	public List<long[]> getResults();

}
