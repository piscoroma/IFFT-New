package it.ifttt.springSocialMongo;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mortbay.log.Log;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;

/**
 * {@link UsersConnectionRepository} that uses the JDBC API to persist connection data to a relational database.
 * The supporting schema is defined in JdbcMultiUserConnectionRepository.sql.
 */
public class MongoUsersConnectionRepository implements UsersConnectionRepository {

	private static Logger log = LogManager.getLogger();
	
	private final ConnectionService mongoService;

	private final ConnectionFactoryLocator connectionFactoryLocator;

	private final TextEncryptor textEncryptor;

	private ConnectionSignUp connectionSignUp;

	public MongoUsersConnectionRepository(ConnectionService mongoService, ConnectionFactoryLocator connectionFactoryLocator, TextEncryptor textEncryptor) {	
		this.mongoService = mongoService;
		this.connectionFactoryLocator = connectionFactoryLocator;
		this.textEncryptor = textEncryptor;
	}

	public void setConnectionSignUp(ConnectionSignUp connectionSignUp) {
		this.connectionSignUp = connectionSignUp;
	}

	@Override
	public List<String> findUserIdsWithConnection(Connection<?> connection) {
		ConnectionKey key = connection.getKey();
		List<String> localUserIds = mongoService.getUserIds(key.getProviderId(), key.getProviderUserId());
		if (localUserIds.size() == 0 && connectionSignUp != null) {
			String newUserId = connectionSignUp.execute(connection);
			if (newUserId != null)
			{
				createConnectionRepository(newUserId).addConnection(connection);
				return Arrays.asList(newUserId);
			}
		}
		return localUserIds;
	}

	@Override
	public Set<String> findUserIdsConnectedTo(String providerId, Set<String> providerUserIds) {
		return mongoService.getUserIds(providerId, providerUserIds);
	}

	@Override
	public ConnectionRepository createConnectionRepository(String userId) {
		log.info("userID: " + userId);
		if (userId == null) {
			throw new IllegalArgumentException("userId cannot be null");
		}
		return new MongoConnectionRepository(userId, mongoService, connectionFactoryLocator, textEncryptor);
	}

}