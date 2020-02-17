package com.rest.app.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.rest.app.entity.User;
import com.rest.app.helpers.CalculationHelper;
import com.rest.app.model.ErrorResponse;
import com.rest.app.model.IResponse;
import com.rest.app.model.Response;

@Service
public class ResponseService {
	private static final Logger LOG = LoggerFactory.getLogger(ResponseService.class);

	private static final String GITHUB_URL = "https://api.github.com/users/";
	private final SessionFactory sessionFactory;

	public ResponseService() {
		final Configuration configuration = new Configuration().configure();
		configuration.addAnnotatedClass(User.class);
		final StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
		sessionFactory = configuration.buildSessionFactory(registryBuilder.build());
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public IResponse processRequest(final String user) {
		LOG.info("Retrieving information from GitHub...");
		JSONObject json;
		try {
			json = retrieveGithubInfo(user);
			if (json == null) {
				new ErrorResponse("There was an error during retrieving info from info");
			}
		}
		catch (final Exception e) {
			LOG.error(e.getMessage());
			return new ErrorResponse("There was an error during retrieving info from info");
		}

		LOG.info("Creating response object...");
		final Response response = new Response();
		try {
			response.setId(Integer.toString(json.getInt("id")));
			response.setLogin(json.getString("login"));
			response.setName(json.getString("name"));
			response.setType(json.getString("type"));
			response.setAvatarUrl(json.getString("avatar_url"));
			response.setCreatedAt(json.getString("created_at"));

			final BigDecimal followers = BigDecimal.valueOf(json.getInt("followers"));
			final BigDecimal publicRepos = BigDecimal.valueOf(json.getInt("public_repos"));
			System.out.println(followers);
			System.out.println(publicRepos);
			response.setCalculations(followers.compareTo(BigDecimal.ZERO) == 0 ? "-1" : CalculationHelper.calculate(followers, publicRepos));
		}
		catch (final JSONException e) {
			LOG.error(e.getMessage());
			return new ErrorResponse("Error during processing response from GitHub");
		}

		LOG.info("Saving to DB...");
		try {
			updateDb(user);
		}
		catch (final Exception e) {
			LOG.error(e.getMessage());
			return new ErrorResponse("Error during saving to database");
		}

		return response;
	}

	private JSONObject retrieveGithubInfo(final String user) throws ClientProtocolException, IOException, ParseException, JSONException {
		final RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(1000).setConnectTimeout(1000).setSocketTimeout(1000).build();
		final HttpClientBuilder builder = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig);
		final CloseableHttpClient client = builder.build();
		final CloseableHttpResponse response = client.execute(new HttpGet(GITHUB_URL + user));
		if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK || response.getEntity() == null) {
			return null;
		}

		return new JSONObject(EntityUtils.toString(response.getEntity()));
	}

	private void updateDb(final String login) {
		final Session session = sessionFactory.openSession();
		session.beginTransaction();

		final CriteriaBuilder builder = session.getCriteriaBuilder();
		final CriteriaQuery<User> criteria = builder.createQuery(User.class);
		final Root<User> myObjectRoot = criteria.from(User.class);
		criteria.select(myObjectRoot).where(builder.equal(myObjectRoot.get("login"), login));

		final TypedQuery<User> query = session.createQuery(criteria);
		final List<User> results = query.getResultList();

		final User user;
		if (results.size() == 0) {
			user = new User();
			user.setLogin(login);
			user.setRequestCount(1);
		}
		else {
			user = results.get(0);
			user.setRequestCount(user.getRequestCount() + 1);
		}

		session.save(user);

		session.getTransaction().commit();
		session.clear();
	}
}
