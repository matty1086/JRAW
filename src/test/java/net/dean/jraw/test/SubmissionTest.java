package net.dean.jraw.test;

import net.dean.jraw.ApiException;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkException;
import net.dean.jraw.models.LoggedInAccount;
import net.dean.jraw.models.core.Comment;
import net.dean.jraw.models.core.Listing;
import net.dean.jraw.models.core.Submission;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

public class SubmissionTest {
	private static final String ID = "92dd8";
	private RedditClient reddit;

	@BeforeClass
	public void setUp() {
		reddit = TestUtils.client(SubmissionTest.class);
	}

	@Test
	public void testCommentsNotNull() {
		try {
			Submission submission = reddit.getSubmission(ID);
			assertNotNull(submission);

			Listing<Comment> comments = submission.getComments();
			assertNotNull(comments, "Submission comments was null");
			// This is the most upvoted link in reddit history, there's bound to be more than one comment
			assertFalse(comments.getChildren().isEmpty());

			Comment first = comments.getChildren().get(0);
			ThingFieldTest.fieldValidityCheck(first);
		} catch (NetworkException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testRepliesNotNull() {
		try {
			Submission s = reddit.getSubmission(ID);

			Comment c = s.getComments().getChildren().get(0);
			System.out.println(c.getReplies().getChildren().get(0).getBody());
		} catch (NetworkException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testSendRepliesToInbox() throws ApiException {
		try {
			Submission s = reddit.getSubmission(ID);
			LoggedInAccount me = reddit.login(TestUtils.getCredentials()[0], TestUtils.getCredentials()[1]);
			me.setSendRepliesToInbox(s, true);
		} catch (NetworkException e) {
			Assert.fail(e.getMessage());
		}
	}
}
