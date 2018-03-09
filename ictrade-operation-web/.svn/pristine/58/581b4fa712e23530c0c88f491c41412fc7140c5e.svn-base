var gulp = require('gulp'),
config = require('./gulp.config'),
rev = require('gulp-rev'),
minifycss = require('gulp-minify-css'),
uglify = require('gulp-uglify'),
concat = require('gulp-concat'),
sourcemaps = require('gulp-sourcemaps'),
del = require('del'),
//jshint = require('gulp-jshint'),
//csslint = require('gulp-csslint'),
imagemin = require('gulp-imagemin'),
revreplace = require('gulp-rev-replace');

gulp.task('clean', function(){
  del([config.css.rev,
      config.js.rev,
      config.css.dist,
      config.js.dist,
      config.rev.dist], function(err, deletedFiles) {
      console.log('Files deleted:\n', deletedFiles.join('\n'));
  });
})

gulp.task('css', function(){
  return gulp.src(config.css.src)
    // .pipe(csslint())
    // .pipe(csslint.formatter())
    .pipe(minifycss({advanced: false,keepSpecialComments: '*'}))
    .pipe(rev())
    .pipe(gulp.dest(config.css.dist))
    .pipe(rev.manifest({base:'',merge: true}))
    .pipe(gulp.dest(config.css.rev));
});

gulp.task('script',['css'], function(){
  return gulp.src(config.js.src)
    // .pipe(jshint())
    // .pipe(jshint.reporter("jshint-stylish"))
//    .pipe(uglify())
    .pipe(rev())
    .pipe(gulp.dest(config.js.dist))
    .pipe(rev.manifest({base:'', merge: true}))
    .pipe(gulp.dest(config.js.rev));
});

gulp.task('imagemin', function(){
  return gulp.src(config.image.src)
    .pipe(imagemin())
    .pipe(gulp.dest(config.image.dist));
});

gulp.task('page', ['script','imagemin'],function(){
  var manifest = gulp.src(config.rev.revJson);
  return gulp.src(config.rev.src)
  .pipe(revreplace({replaceInExtensions: ['.jsp'], manifest: manifest}))
  .pipe(gulp.dest(config.rev.dist));
});

gulp.task('default', ['clean','page'], function() {
// place code for your default task here
});
