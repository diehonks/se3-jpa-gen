package unittests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import persistence.daos.StudyPathDAO;
import persistence.entities.Address;
import persistence.entities.StudyPath;
import persistence.entities.StudyTheme;

public class TestStudyPath {

	private StudyPathDAO pathDAO = null;
	private long pathID = 0;

	@BeforeClass
	public void initTest() {
		pathDAO = new StudyPathDAO();
	}

	@Test
	public void createStudyPath() {
		final StudyPath path = new StudyPath();
		path.setName("TestName");
		path.setTheme(StudyTheme.INFORMATICS);
		pathID = path.getId();
		Assert.assertEquals(pathID, 0);
		pathID = pathDAO.createStudyPath(path).getId();
		Assert.assertEquals((pathID > 0), true);
		Assert.assertEquals(pathID != pathDAO.createStudyPath(new StudyPath())
				.getId(), true);
	}

	@Test(dependsOnMethods = "createStudyPath")
	public void readStudyPath() {
		final StudyPath path = pathDAO.readStudyPath(pathID);
		Assert.assertEquals(path.getName(), "TestName");
		Assert.assertEquals(path.getTheme(), StudyTheme.INFORMATICS);
		int count = pathDAO.readAllStudyPath().size();
		Assert.assertEquals(count, 2);
		pathDAO.createStudyPath(new StudyPath());
		count = pathDAO.readAllStudyPath().size();
		Assert.assertEquals(count, 3);
	}

	@Test(dependsOnMethods = "readStudyPath")
	public void updateStudyPath() {
		final StudyPath path = pathDAO.readStudyPath(pathID);
		path.setName("NewTestName");
		path.setTheme(StudyTheme.ENGINEERING);
		pathDAO.updateStudyPath(path);
		final StudyPath samePath = pathDAO.readStudyPath(pathID);
		Assert.assertEquals(samePath.getName(), "NewTestName");
		Assert.assertEquals(samePath.getTheme(), StudyTheme.ENGINEERING);
	}

	@Test(dependsOnMethods = "updateStudyPath")
	public void deleteStudyPath() {
		StudyPath path = pathDAO.readStudyPath(pathID);
		int count = pathDAO.readAllStudyPath().size();
		Assert.assertEquals(count, 3);
		pathDAO.deleteStudyPath(path);
		count = pathDAO.readAllStudyPath().size();
		Assert.assertEquals(count, 2);
		path = pathDAO.readStudyPath(pathID);
		Assert.assertNull(path);
	}
}
