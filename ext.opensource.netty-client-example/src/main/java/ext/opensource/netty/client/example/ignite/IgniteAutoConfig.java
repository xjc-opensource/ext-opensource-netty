package ext.opensource.netty.client.example.ignite;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteMessaging;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.DataRegionConfiguration;
import org.apache.ignite.configuration.DataStorageConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import io.netty.util.internal.StringUtil;
import java.util.Arrays;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

@Configuration
public class IgniteAutoConfig {
	@Value("${spring.ignite.id: xx01}")
	private String instanceName;

	@Value("${spring.ignite.enable-multicast-group: false}")
	private boolean enableMulticastGroup;

	@Value("${spring.ignite.multicast-group: 239.255.255.255}")
	private String multicastGroup;

	@Value("${spring.ignite.static-ip-addresses:127.0.0.1}")
	private String[] staticIpAddresses;

	@Bean
	public IgniteProperties igniteProperties() {
		return new IgniteProperties();
	}

	@Bean
	@Conditional(IgniteConditionalCache.class)
	public Ignite ignite() throws Exception {
		IgniteConfiguration cfg = new IgniteConfiguration();
		// Ignite实例名称
		cfg.setIgniteInstanceName(instanceName);
		cfg.setMetricsLogFrequency(0);
		// Ignite日志
	    //Logger logger = LoggerFactory.getLogger("org.apache.ignite");
        //igniteConfiguration.setGridLogger(new Slf4jLogger(logger));
		// 非持久化数据区域
		DataRegionConfiguration notPersistence = new DataRegionConfiguration().setPersistenceEnabled(false)
			.setInitialSize(igniteProperties().getNotPersistenceInitialSize() * 1024 * 1024)
			.setMaxSize(igniteProperties().getNotPersistenceMaxSize() * 1024 * 1024).setName("not-persistence-data-region");
		// 持久化数据区域
		DataRegionConfiguration persistence = new DataRegionConfiguration().setPersistenceEnabled(true)
			.setInitialSize(igniteProperties().getPersistenceInitialSize() * 1024 * 1024)
			.setMaxSize(igniteProperties().getPersistenceMaxSize() * 1024 * 1024).setName("persistence-data-region");
		
		String storepath = igniteProperties().getPersistenceStorePath();
		if (StringUtil.isNullOrEmpty(storepath)) {
			storepath = null;
		}
		DataStorageConfiguration dataStorageConfiguration = new DataStorageConfiguration().setDefaultDataRegionConfiguration(notPersistence)
			.setDataRegionConfigurations(persistence)
			.setWalArchivePath(storepath)
			.setWalPath(storepath)
			.setStoragePath(storepath);
		
		cfg.setDataStorageConfiguration(dataStorageConfiguration);
		// 集群, 基于组播或静态IP配置
		TcpDiscoverySpi tcpDiscoverySpi = new TcpDiscoverySpi();
		if (this.enableMulticastGroup) {
			TcpDiscoveryMulticastIpFinder tcpDiscoveryMulticastIpFinder = new TcpDiscoveryMulticastIpFinder();
			tcpDiscoveryMulticastIpFinder.setMulticastGroup(multicastGroup);
			tcpDiscoverySpi.setIpFinder(tcpDiscoveryMulticastIpFinder);
		} else {
			TcpDiscoveryVmIpFinder tcpDiscoveryVmIpFinder = new TcpDiscoveryVmIpFinder();
			tcpDiscoveryVmIpFinder.setAddresses(Arrays.asList(staticIpAddresses));
			tcpDiscoverySpi.setIpFinder(tcpDiscoveryVmIpFinder);
		}
		cfg.setDiscoverySpi(tcpDiscoverySpi);
		Ignite ignite = Ignition.start(cfg);
		ignite.cluster().active(true);
		return ignite;
	}

	@Bean
	@Conditional(IgniteConditionalCache.class)
	public IgniteCache<?, ?> messageIdCache() throws Exception {
		@SuppressWarnings("rawtypes")
		CacheConfiguration<?, ?> cacheConfiguration = new CacheConfiguration().setDataRegionName("not-persistence-data-region")
			.setCacheMode(CacheMode.LOCAL).setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL).setName("messageIdCache");
		return ignite().getOrCreateCache(cacheConfiguration);
	}

	@Bean
	@Conditional(IgniteConditionalCache.class)
	public IgniteCache<?, ?> messageCache() throws Exception {
		@SuppressWarnings("rawtypes")
		CacheConfiguration<?, ?> cacheConfiguration = new CacheConfiguration().setDataRegionName("persistence-data-region")
			.setCacheMode(CacheMode.LOCAL).setName("retainMessageCache");
		return ignite().getOrCreateCache(cacheConfiguration);
	}

	@Bean
	@Conditional(IgniteConditionalCache.class)
	public IgniteMessaging igniteMessaging() throws Exception {
		return ignite().message(ignite().cluster().forRemotes());
	}
}
