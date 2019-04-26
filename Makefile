RELEASE_VERSION ?= latest

SUBDIRS=ui-trade-manager price-feed-connector ui-price-viewer trade-aggregator ui-portfolio-viewer pricer ui-priced-portfolio-viewer
DOCKER_TARGETS=docker_build docker_push docker_tag

all: $(SUBDIRS)
build: $(SUBDIRS)
clean: $(SUBDIRS)
$(DOCKER_TARGETS): $(SUBDIRS)

$(SUBDIRS):
	$(MAKE) -C $@ $(MAKECMDGOALS)

.PHONY: all $(SUBDIRS) $(DOCKER_TARGETS)
