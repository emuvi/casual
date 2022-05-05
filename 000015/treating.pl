#!/usr/bin/perl
use warnings;
use strict;

my $starting = "./treating/";
open(SRC, '<', 'index.html') or die $!;
while(<SRC>) {
  if(/<a href="(.*?)">(.*?)<\/a>/) {
    my $link = $1;
    print "Found link: $link\n";
    if (rindex $link, $starting, 0 eq -1) {
      print "The link does not starts with: $starting\n";
      next;
    }
    if (-e $link){
      print "File $link already exists.\n";
      next;
    }
    my $title = $2;
    my $treat = qq{<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta http-equiv="X-UA-Compatible" content="IE=edge" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Éverton M. Vieira</title>
  <link rel="stylesheet" href="../styles.css" />
</head>

<body>
  <div id="divBody">
    <div id="divLittleTopBar"></div>
    <div id="divContent">
      <div class="divRowContent">
        <div class="divColExpand">
          <div id="divNavBar">
            <a class="top-link" href="http://emuvi.github.io">emuvi.github.io</a> |
            <a class="top-link" href="http://emuvi.blog.br"
              target="_blank">emuvi.blog.br</a> |
            <a class="top-link"
              href="mailto:everton.muvi\@gmail.com">everton.muvi\@gmail.com</a> |
            <button onclick="history.back()">return</button>
          </div>
          <h1>$title</h1>
          <p>Processing a small essay about it...</p>
        </div>
      </div>
    </div>
  </div>
</body>
</html>};
    open(DES, '>', $link) or die $!;
    print DES $treat;
    close(DES);
    print "Generated $link.\n";
  }
}
close(SRC);