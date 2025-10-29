"use client";

import * as React from "react";
import { MessageCircleQuestion } from "lucide-react";

import { PageHeader } from "@/components/page-header";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Textarea } from "@/components/ui/textarea";
import { useToast } from "@/components/ui/use-toast";

export default function HelpPage() {
  const { addToast } = useToast();
  const [message, setMessage] = React.useState("");

  const handleSubmit = (event: React.FormEvent) => {
    event.preventDefault();
    addToast({ title: "Support ticket created", description: "We'll respond within one business day." });
    setMessage("");
  };

  return (
    <div className="space-y-6">
      <PageHeader title="Help centre" description="Guides, accessibility support, and live chat." />
      <Card>
        <CardHeader>
          <CardTitle className="flex items-center gap-2 text-xl">
            <MessageCircleQuestion className="h-5 w-5" aria-hidden="true" /> Submit a support request
          </CardTitle>
        </CardHeader>
        <CardContent>
          <form className="space-y-4" onSubmit={handleSubmit}>
            <Textarea
              value={message}
              onChange={(event) => setMessage(event.target.value)}
              placeholder="Describe the issue you're facing..."
              required
            />
            <div className="flex items-center justify-end gap-3">
              <Button type="submit">Send</Button>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  );
}
