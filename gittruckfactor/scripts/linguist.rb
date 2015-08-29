require 'rugged'
require 'linguist'
repo = Rugged::Repository.new(ARGV[0])
project = Linguist::Repository.new(repo, repo.head.target_id)
result = project.breakdown_by_file
result.each { |l, fs| 
  fs.each { |f| 
    puts "#{l};#{f}" }
}
#puts result