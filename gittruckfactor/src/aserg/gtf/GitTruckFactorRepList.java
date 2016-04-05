package aserg.gtf;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import aserg.gtf.model.LogCommitInfo;
import aserg.gtf.model.NewFileInfo;
import aserg.gtf.model.authorship.Repository;
import aserg.gtf.task.DOACalculator;
import aserg.gtf.task.NewAliasHandler;
import aserg.gtf.task.extractor.FileInfoExtractor;
import aserg.gtf.task.extractor.GitLogExtractor;
import aserg.gtf.task.extractor.LinguistExtractor;
import aserg.gtf.truckfactor.GreedyTruckFactor;
import aserg.gtf.truckfactor.TruckFactor;
import aserg.gtf.util.FileInfoReader;
import aserg.gtf.util.LineInfo;

public class GitTruckFactorRepList {

	public static void main(String[] args) throws IOException {
		String repArray[] = {"activeadmin/activeadmin",	"ajaxorg/ace",	"alexreisner/geocoder",	"android/platform_frameworks_base",	"ansible/ansible",	"apache/cassandra",	"atom/atom-shell",	"bbatsov/rubocop",	"bitcoin/bitcoin",	"bjorn/tiled",	"boto/boto",	"bumptech/glide",	"bundler/bundler",	"bup/bup",	"BVLC/caffe",	"caskroom/homebrew-cask",	"celery/celery",	"celluloid/celluloid",	"chef/chef",	"clojure/clojure",	"cocos2d/cocos2d-x",	"codemirror/CodeMirror",	"composer/composer",	"cucumber/cucumber",	"diaspora/diaspora",	"divio/django-cms",	"django/django",	"driftyco/ionic",	"dropwizard/dropwizard",	"dropwizard/metrics",	"drupal/drupal",	"elasticsearch/elasticsearch",	"elasticsearch/logstash",	"emberjs/ember.js",	"erikhuda/thor",	"Eugeny/ajenti",	"excilys/androidannotations",	"facebook/osquery",	"facebook/presto",	"fog/fog",	"FriendsOfPHP/PHP-CS-Fixer",	"fzaninotto/Faker",	"getsentry/sentry",	"git/git",	"github/android",	"github/linguist",	"gradle/gradle",	"gruntjs/grunt",	"haml/haml",	"Homebrew/homebrew",	"iojs/io.js",	"ipython/ipython",	"Itseez/opencv",	"jadejs/jade",	"janl/mustache.js",	"jashkenas/backbone",	"jekyll/jekyll",	"JetBrains/intellij-community",	"jnicklas/capybara",	"JohnLangford/vowpal_wabbit",	"joomla/joomla-cms",	"jquery/jquery",	"jquery/jquery-ui",	"jrburke/requirejs",	"justinfrench/formtastic",	"kivy/kivy",	"koush/ion",	"kriswallsmith/assetic",	"Leaflet/Leaflet",	"less/less.js",	"libgdx/libgdx",	"mailpile/Mailpile",	"mbostock/d3",	"meskyanichi/backup",	"meteor/meteor",	"mitchellh/vagrant",	"mitsuhiko/flask",	"moment/moment",	"mongoid/mongoid",	"mozilla/pdf.js",	"mrdoob/three.js",	"nate-parrott/Flashlight",	"netty/netty",	"nicolasgramlich/AndEngine",	"odoo/odoo",	"omab/django-social-auth",	"openframeworks/openFrameworks",	"paulasmuth/fnordmetric",	"phacility/phabricator",	"php/php-src",	"plataformatec/devise",	"powerline/powerline",	"prawnpdf/prawn",	"puphpet/puphpet",	"puppetlabs/puppet",	"pydata/pandas",	"rails/rails",	"ratchetphp/Ratchet",	"ReactiveX/RxJava",	"Respect/Validation",	"resque/resque",	"rg3/youtube-dl",	"ruby/ruby",	"saltstack/salt",	"sampsyo/beets",	"sandstorm-io/capnproto",	"sass/sass",	"scikit-learn/scikit-learn",	"sebastianbergmann/phpunit",	"Seldaek/monolog",	"sferik/twitter",	"SFTtech/openage",	"Shopify/active_merchant",	"silexphp/Silex",	"sparklemotion/nokogiri",	"spotify/luigi",	"spring-projects/spring-framework",	"sstephenson/sprockets",	"strongloop/express",	"substack/node-browserify",	"thinkaurelius/titan",	"ThinkUpLLC/ThinkUp",	"thoughtbot/factory_girl",	"thoughtbot/paperclip",	"thumbor/thumbor",	"torvalds/linux",	"TryGhost/Ghost",	"v8/v8",	"webscalesql/webscalesql-5.6",	"WordPress/WordPress",	"wp-cli/wp-cli",	"xetorthio/jedis",	"yiisoft/yii2"};
		

		System.out.println("BEGIN at " + new Date() + "\n\n");
		
		//fileExtractor.persist(files);
		String step = "begin";
		String repositoryPath;
		String repositoryName = "";
		for (int i = 0; i < repArray.length; i++) {
			try {
				step = "begin";
				repositoryPath = "E:/backups/icse/icpc-selected-repos-logs/"+repArray[i].replace('/', '-')+"/";
				repositoryName = repArray[i];
				
				
				Map<String, List<LineInfo>> filesInfo = FileInfoReader
						.getFileInfo("repo_info/filtered-files.txt");
				Map<String, List<LineInfo>> aliasInfo = FileInfoReader
						.getFileInfo("repo_info/alias.txt");
				
				
				GitLogExtractor gitLogExtractor = new GitLogExtractor(
						repositoryPath, repositoryName);
				//		AliasHandler aliasHandler = new AliasHandler(aliasInfo.get(repositoryName));
				NewAliasHandler aliasHandler = new NewAliasHandler(
						aliasInfo.get(repositoryName));
				FileInfoExtractor fileExtractor = new FileInfoExtractor(
						repositoryPath, repositoryName);
				LinguistExtractor linguistExtractor = new LinguistExtractor(
						repositoryPath, repositoryName);
				
				step = "logextractor"; 
				Map<String, LogCommitInfo> commits = gitLogExtractor.execute();
				step = "aliashandler";
				commits = aliasHandler.execute(repositoryName, commits);
//				step = "commits persist";
//				gitLogExtractor.persist(commits);
				
				step = "fileExtractor";
				List<NewFileInfo> files = fileExtractor.execute();
				step = "linguist";
				files = linguistExtractor.setNotLinguist(files);
//				step = "files persist";
//				fileExtractor.persist(files);
				
				//applyFilterFiles(filesInfo.get(repositoryName), files);
				//applyRegexFilter(files, "^src/Faker/Provider/.*");
				//applyRegexSelect(files, "^kernel/.*");
				step = "DOA Calculator";
				DOACalculator doaCalculator = new DOACalculator(repositoryPath,
						repositoryName, commits.values(), files);
				Repository repository = doaCalculator.execute();
				//step = "Doa Persist";
				//doaCalculator.persist(repository);
				
				step = "TF";
				TruckFactor truckFactor = new GreedyTruckFactor();
				System.out.println(repositoryName+": ");
				truckFactor.getTruckFactor(repository);
			} catch (Exception e) {
				System.err.format("\nException in GitTruckFactor: %s step: %s\n", repositoryName, step);
				e.printStackTrace();
			}
		}
		System.out.println("\n\nEND at "+ new Date());
	}

	private static void applyRegexFilter(List<NewFileInfo> files, String exp) {
		int count = 0;
		for (NewFileInfo newFileInfo : files) {
			if (!newFileInfo.getFiltered()){
				if (newFileInfo.getPath().matches(exp)){
					count++;
					newFileInfo.setFiltered(true);
					newFileInfo.setFilterInfo("REGEX: "+ exp);
				}			
			}
		}
		System.out.println("REGEX FILTER = " + count);
	}
	
	private static void applyRegexSelect(List<NewFileInfo> files, String exp) {
		int count = 0;
		for (NewFileInfo newFileInfo : files) {
			if (!newFileInfo.getFiltered()){
				if (!newFileInfo.getPath().matches(exp)){
					count++;
					newFileInfo.setFiltered(true);
					newFileInfo.setFilterInfo("REGEX: "+ exp);
				}			
			}
		}
		System.out.println("REGEX FILTER = " + count);
	}

	private static void applyFilterFiles(List<LineInfo> filteredFilesInfo, List<NewFileInfo> files) {
		if (filteredFilesInfo != null ){
			for (LineInfo lineInfo : filteredFilesInfo) {
				String path = lineInfo.getValues().get(0);
				for (NewFileInfo newFileInfo : files) {
					if (newFileInfo.getPath().equals(path)){
						newFileInfo.setFiltered(true);
						newFileInfo.setFilterInfo(lineInfo.getValues().get(1));
						System.out.println(path);
					}
					
				}
			}
		}
	}

}
